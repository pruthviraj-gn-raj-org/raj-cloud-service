package com.raj.cloud.service.utils;

public class FileUtils {

	private static final long  KILOBYTE = 1024L;
	private static final long  MEGABYTE = 1024L * 1024L;
	private static final long  GIGABYTE = 1024L * 1024L * 1024L;
	private static final long  TERABYTE = 1024L * 1024L * 1024L * 1024L;
	private static final long  PETABYTE = 1024L * 1024L * 1024L * 1024L * 1024L;
	
	
	public static long bytesToKb(long bytes)
	{
		return bytes/KILOBYTE;
	}
	
	
	public static long bytesToMb(long bytes)
	{
		return bytes/MEGABYTE;
	}
	
	
	public static long bytesToGb(long bytes)
	{
		return bytes/GIGABYTE;
	}
	
	public static long bytesToTb(long bytes)
	{
		return bytes/TERABYTE;
	}
	
	public static long bytesToPb(long bytes)
	{
		return bytes/PETABYTE;
	}
}
