package com.yoga.demo.hadoop.test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.jobcontrol.ControlledJob;
import org.apache.hadoop.mapreduce.lib.jobcontrol.JobControl;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class MapReduceTest {
	
	//1、设定输入目录
	 public static final String INPUT_PATH = "hdfs://192.168.4.128:9000/input/wordcount.txt";
	 //2、设定输出目录
	 public static final String OUTPUT_PATH = "hdfs://192.168.4.128:9000/output/wordcount";
	
	 /**
	  * map()方法（ maptask 进程）对每一个<K,V>调用一次
	  * 
	  * maptask 阶段处理每个数据分块的单词统计分析，
	  * 思路是每遇到一个单词则把其转换成 一个 key-value 对，
	  * 比如单词 hello，就转换成<’hello’,1>发送给 reducetask 去汇总
	  * 
	  * @author yoga
	  */
	 public static class MyMapper extends Mapper<LongWritable, Text, Text, LongWritable> {
		 protected void map(LongWritable key, Text value,Mapper<LongWritable, Text, Text, LongWritable>.Context context)
    		 throws java.io.IOException, InterruptedException {
			 
			     String[] spilted = value.toString().split(" ");
			     for (String word : spilted) {
			         context.write(new Text(word), new LongWritable(1L));
			     }
			     
		 	};
 	}
	 
	 /**
	  * Reducetask 进程对每一组相同 k 的<k,v>组调用一次 reduce()方法
	  * 
	  * reducetask 阶段将接受 maptask 的结果， 来做汇总计数
	  * 
	  * @author yoga
	  */
	 public static class MyReducer extends
	     Reducer<Text, LongWritable, Text, LongWritable> {
		 protected void reduce(Text key,
	         java.lang.Iterable<LongWritable> values,
	         Reducer<Text, LongWritable, Text, LongWritable>.Context context)
	         throws java.io.IOException, InterruptedException {
			 
			     long count = 0L;
			     for (LongWritable value : values) {
			         count += value.get();
			     }
			     context.write(key, new LongWritable(count));
			     
		 	};
	 }
	 
	 public static void main(String[] args) throws IOException, URISyntaxException, ClassNotFoundException, InterruptedException {
		 Configuration conf = HBaseConfiguration.create();
		 
		// 0.0:首先删除输出路径的已有生成文件
	        FileSystem fs = FileSystem.get(new URI(INPUT_PATH), conf);
	        Path outPath = new Path(OUTPUT_PATH);
	        if (fs.exists(outPath)) {
	            fs.delete(outPath, true);
	        }
	        
	        //新建一个job任务
	        Job job = Job.getInstance(conf, "WordCount");
	        //设置jar包所在路径
	        job.setJarByClass(MapReduceTest.class);
	        
	        //指定输入目录
	        FileInputFormat.setInputPaths(job, new Path(INPUT_PATH));
	        
	        //指定mapper和reducer 类
	        job.setMapperClass(MyMapper.class);
	        job.setReducerClass(MyReducer.class);
	        
	        //指定map task输出类型
	        job.setMapOutputKeyClass(Text.class);
	        job.setMapOutputValueClass(LongWritable.class);
	        
	        //设置要运行的Reducer的数量（可以省略）
//	        job.setNumReduceTasks(1);
	        
	        //指定reduce task输出类型
	        job.setOutputKeyClass(Text.class);
	        job.setOutputValueClass(LongWritable.class);
	        
	        //指定输出目录
	        FileOutputFormat.setOutputPath(job, new Path(OUTPUT_PATH));
	        
//	        //多个任务同时启动，通过JobControl监控任务执行状态
//	        ControlledJob cJob = new ControlledJob(conf);
//	        cJob.setJob(job);
//	        
//	        JobControl jobControl = new JobControl("groupName");
//	        jobControl.addJob(cJob);
//	        
//	        new Thread(jobControl).start();
//	        
//	        if(jobControl.allFinished()){
//				//如果作业成功完成，就打印成功作业的信息   
//				System.out.println(jobControl.getSuccessfulJobList());   
//				jobControl.stop();
//			}
	        
	        System.exit(job.waitForCompletion(true) ? 0 : 1); 
	}
}
