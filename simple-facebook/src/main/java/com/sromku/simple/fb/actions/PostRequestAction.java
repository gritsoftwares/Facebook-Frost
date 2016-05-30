package com.sromku.simple.fb.actions;

import com.facebook.AccessToken;
import com.facebook.FacebookRequestError;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.sromku.simple.fb.SessionManager;
import com.sromku.simple.fb.listeners.OnPostListener;
import com.sromku.simple.fb.utils.Errors;
import com.sromku.simple.fb.utils.Errors.ErrorMsg;
import com.sromku.simple.fb.utils.Logger;

public class PostRequestAction extends AbstractAction {

    private OnPostListener mOnPostListener;
    private String mRequestId;

    /*
     * Allan Wang 2016/05/29
     * Replica of delete request action
     * meant for basic requests
     */

    public PostRequestAction(SessionManager sessionManager) {
        super(sessionManager);
    }

    public void setRequestId(String requestId) {
        mRequestId = requestId;
    }

    public void setOnPostListener(OnPostListener onPostRequestListener) {
        mOnPostListener = onPostRequestListener;
    }

    @Override
    protected void executeImpl() {
        if (sessionManager.isLogin()) {
            AccessToken accessToken = sessionManager.getAccessToken();
            GraphRequest request = new GraphRequest(accessToken, mRequestId, null, HttpMethod.POST, new GraphRequest.Callback() {
                @Override
                public void onCompleted(GraphResponse response) {
                    FacebookRequestError error = response.getError();
                    if (error != null) {
                        Logger.logError(PostRequestAction.class, "failed to post requests", error.getException());
                        if (mOnPostListener != null) {
                            mOnPostListener.onException(error.getException());
                        }
                    }
                    else {
                        if (mOnPostListener != null) {
                            mOnPostListener.onComplete(null);
                        }
                    }
                }
            });
            GraphRequest.executeBatchAsync(request);
        }
        else {
            String reason = Errors.getError(ErrorMsg.LOGIN);
            Logger.logError(PostRequestAction.class, reason, null);
            if (mOnPostListener != null) {
                mOnPostListener.onFail(reason);
            }
        }
    }

}
