package com.shichai.www.choume.network.task.cfuser;

import com.globalways.choume.proto.CFAppUserServiceGrpc;
import com.globalways.choume.proto.nano.OutsouringCrowdfunding.CfUserHistoriesParam;
import com.globalways.choume.proto.nano.OutsouringCrowdfunding.CfUserHistoriesResp;
import com.shichai.www.choume.network.CMChannel;
import com.shichai.www.choume.network.task.CommonTask;

import java.util.concurrent.TimeUnit;

/**
 * Created by wyp on 15/12/22.
 */
public class CfUserHistoriesTask extends CommonTask<CfUserHistoriesParam,CfUserHistoriesResp> {
    @Override
    protected CfUserHistoriesResp doInBackground(Object... params) {
        try {
            channel = CMChannel.buildCM();
            CFAppUserServiceGrpc.CFAppUserServiceBlockingStub stub = CFAppUserServiceGrpc.newBlockingStub(channel);
            return stub.cfUserHistories(taskParam);
        } catch (Exception e) {
            exception = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(CfUserHistoriesResp response) {
        try {
            channel.shutdown().awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        if (response == null) {
            callBack.error(exception);
        }else if (response.resp.code != 1) {
            callBack.warning((int)response.resp.code,response.resp.msg);
        }else {
            callBack.success(response);
        }
    }
}
