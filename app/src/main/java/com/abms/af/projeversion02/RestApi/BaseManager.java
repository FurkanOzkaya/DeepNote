package com.abms.af.projeversion02.RestApi;

public class BaseManager {

    protected RestApi getRestApiClient()
    {
        RestApiClient restApiClient=new RestApiClient(BaseUrl.bilgiurl);
        return  restApiClient.getmRestAApi();
    }
}
