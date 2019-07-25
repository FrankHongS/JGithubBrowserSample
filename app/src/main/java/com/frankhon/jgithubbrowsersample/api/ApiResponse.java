package com.frankhon.jgithubbrowsersample.api;

import android.text.TextUtils;

import com.frankhon.jgithubbrowsersample.util.StringUtil;
import com.hon.mylogger.MyLogger;

import java.util.Map;
import java.util.regex.Matcher;

import retrofit2.Response;

/**
 * Created by Frank_Hon on 7/23/2019.
 * E-mail: v-shhong@microsoft.com
 */
public class ApiResponse {

    public static ApiErrorResponse create(Throwable throwable) {
        return new ApiErrorResponse(throwable.getMessage() == null ? "unknown error" : throwable.getMessage());
    }

    public static <T> ApiResponse create(Response<T> response) {
        ApiResponse target;
        if (response.isSuccessful()) {
            T body = response.body();
            if (body == null || response.code() == 204) {
                target = new ApiEmptyResponse();
            } else {
                String linkHeader = "";
                if (response.headers() != null) {
                    linkHeader = response.headers().get("link");
                }

                target = new ApiSuccessResponse<>(body, linkHeader);
            }
        } else {
            String errorMsg;
            if (response.errorBody() == null || TextUtils.isEmpty(response.errorBody().toString())) {
                errorMsg = response.message();
            } else {
                errorMsg = response.errorBody().toString();
            }

            target = new ApiErrorResponse(TextUtils.isEmpty(errorMsg) ? "unknown error" : errorMsg);
        }

        return target;
    }

    public static class ApiEmptyResponse extends ApiResponse {

    }

    public static class ApiSuccessResponse<T> extends ApiResponse {

        private final T body;
        private final Map<String, String> links;

        private int nextPage = -2;

        ApiSuccessResponse(T body, String linkHeader) {
            this.body = body;
            this.links = TextUtils.isEmpty(linkHeader) ? null : StringUtil.extractLinks(linkHeader);
        }

        public int getNextPage() {
            if (nextPage == -2) {
                String next = links.get(StringUtil.NEXT_LINK);

                if (next != null) {
                    Matcher matcher = StringUtil.PAGE_PATTERN.matcher(next);
                    if (!matcher.find() || matcher.groupCount() != 1) {
                        nextPage = -1;
                    } else {
                        try {
                            nextPage = Integer.parseInt(matcher.group(1));
                        } catch (NumberFormatException e) {
                            MyLogger.w("cannot parse next page from %s", next);
                            nextPage = -1;
                        }
                    }
                }
            }

            return nextPage;
        }

        public T getBody() {
            return body;
        }
    }

    public static class ApiErrorResponse extends ApiResponse {

        private String errorMessage;

        ApiErrorResponse(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }
}
