package com.levi.springai.structuredoutput.entity;

import com.levi.springai.structuredoutput.enums.JobType;

import java.util.Map;

public  record Job(JobType jobType, Map<String,String> keyInfos) {}
