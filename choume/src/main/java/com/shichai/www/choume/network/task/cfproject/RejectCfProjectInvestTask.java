package com.shichai.www.choume.network.task.cfproject;

import com.globalways.choume.proto.CfProjectServiceGrpc;
import com.globalways.choume.proto.nano.OutsouringCrowdfunding.RejectCfProjectInvestParam;
import com.globalways.proto.nano.Common;
import com.shichai.www.choume.network.CMChannel;
import com.shichai.www.choume.network.task.CommonTask;

import java.util.concurrent.TimeUnit;

/**
 * Created by wyp on 15/12/22.
 */
public class RejectCfProjectInvestTask extends CommonTask<RejectCfProjectInvestParam,Common.Response> {
    @Override
    protected Common.Response doInBackground(Object... params) {
        try {
            channel = CMChannel.buildCM();
            CfProjectServiceGrpc.CfProjectServiceBlockingStub stub = CfProjectServiceGrpc.newBlockingStub(channel);
            return stub.rejectCfProjectInvest(taskParam);
        } catch (Exception e) {
            exception = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(Common.Response response) {
        try {
            channel.shutdown().awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        if (response == null) {
            callBack.error(exception);
        }else if (response.code != 1) {
            callBack.warning((int)response.code,response.msg);
        }else {
            callBack.success(response);
        }
    }
}
