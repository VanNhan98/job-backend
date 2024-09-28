package vn.job.dto.response.error;

import vn.job.dto.response.error.ResponseData;

public class ResponseError extends ResponseData {
    public ResponseError(int status, String message) {
        super(status, message);
    }
}
