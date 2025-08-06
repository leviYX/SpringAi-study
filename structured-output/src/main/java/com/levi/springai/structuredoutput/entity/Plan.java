package com.levi.springai.structuredoutput.entity;

import com.levi.springai.structuredoutput.enums.WalkType;

import java.util.Map;

public record Plan (WalkType walkType, Map<String,String> infos) {}
