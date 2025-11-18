package com.supercoding.hrms.com.util;

import java.util.Date;

public record TokenWithMeta(String token, Date issuedAt, Date expiresAt) { }
