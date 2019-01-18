package com.dity.ssm.util;

import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkIndexByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.script.mustache.SearchTemplateRequestBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @author:yuhang
 * @Date:2019/1/17
 */
public class ESUtil {
    private static final String OPERATE_QUERY="query";
    private static final String OPERATE_RANGE="range";
    private static final String OPERATE_FILTER="filter";
    private static final String OPERATE_SORT="sort";
    private static final String OPERATE_PAGE="page";
    private static TransportClient client = null;

    /**
     * 获取ES客户端
     * @return
     */
    public static TransportClient getClient(){
        if ( client == null){
            try {
                Settings settings = Settings.builder().put("client.transport.sniff",true).build();
                client = new PreBuiltTransportClient(settings).addTransportAddress(
                        new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"),9300 )
                );

            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return client;
    }

    /**
     * 保存或更新文档
     * @param index 索引库
     * @param type 映射
     * @param id 文档id
     * @param json 文档内容
     */
    public static void saveOrUpdate(String index,String type,String id,String json){
        IndexRequest indexRequest = new IndexRequest(index,type,id).source(json);
        UpdateRequest updateRequest = new UpdateRequest(index,type,id)
                .doc(json).upsert(json);
        try {
            UpdateResponse updateResponse = getClient().update(updateRequest).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除文档
     * @param index 索引库
     * @param type 文档映射
     * @param id 文档id
     */
    public static void  delete(String index,String type,String id){
        getClient().prepareDelete(index, type, id).get();
    }

    /**
     * 根据id获取文档
     * @param index 索引库
     * @param type 文档映射
     * @param id 文档id
     * @return
     */
    public static Map<String,Object> get(String index,String type,String id){
        GetResponse getResponse = getClient().prepareGet(index, type, id).get();
        if (getResponse.isExists()){
            return getResponse.getSource();
        }else {
            return new HashMap<>();
        }
    }

    /**
     * 根据条件分页查询
     * @param index 索引库
     * @param type 文档映射
     * @param param 搜索条件
     * @return
     */
    public static Page<Map<String,Object>> query(String index,String type,Map<String,Map<String,Object>> param){
        SearchRequestBuilder requestBuilder = getClient().prepareSearch(index).setTypes(type);
        requestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

        List<QueryBuilder> must = queryBuilder.must();
        // 添加条件查询
        if (param.containsKey(OPERATE_QUERY)){
            Map<String, Object> queryParam = param.get(OPERATE_QUERY);
            for (Map.Entry<String, Object> entry : queryParam.entrySet()) {
                must.add(QueryBuilders.matchQuery(entry.getKey(),entry.getValue()));
            }
        }
        // 添加条件过滤
        List<QueryBuilder> filter = queryBuilder.filter();
        if (param.containsKey(OPERATE_FILTER)){
            Map<String, Object> filterParam = param.get(OPERATE_FILTER);
            for (Map.Entry<String, Object> entry : filterParam.entrySet()) {
                filter.add(QueryBuilders.termQuery(entry.getKey(),entry.getValue()));
            }
        }
        // 添加范围查询
        if (param.containsKey(OPERATE_RANGE)){
            Map<String, Object> rangeParam = param.get(OPERATE_RANGE);
            for (Map.Entry<String, Object> entry : rangeParam.entrySet()) {
                String range = (String) entry.getValue();
                String[] ranges = range.split("-");
                filter.add(QueryBuilders.rangeQuery(entry.getKey()).from(Double.parseDouble(ranges[0])).to(Double.parseDouble(ranges[1])));
            }
        }
        requestBuilder.setQuery(queryBuilder);

        // 添加排序
        if (param.containsKey(OPERATE_SORT)){
            Map<String, Object> sortParam = param.get(OPERATE_SORT);
            for (Map.Entry<String, Object> entry : sortParam.entrySet()) {
                SortOrder sortOrder = SortOrder.ASC;
                if ("desc".equals(entry.getValue())){
                    sortOrder = SortOrder.DESC;
                }
                requestBuilder.addSort(entry.getKey(),sortOrder);
            }
        }
        Page<Map<String,Object>> page = null;
        if (param.containsKey(OPERATE_PAGE)){
            Map<String, Object> pageParam = param.get(OPERATE_SORT);
            int pageNum = Integer.parseInt((String) pageParam.get("page"));
            int pageSize = Integer.parseInt((String) pageParam.get("size"));
             new Page<>(pageNum,pageSize);
            requestBuilder.setFrom(page.getStartRow()).setSize(page.getPageSize()).setExplain(false);
        }
        SearchResponse searchResponse = requestBuilder.get();
        long totalHits = searchResponse.getHits().getTotalHits();
        List<Map<String,Object>> data = new ArrayList<>();
        if (totalHits>0){
            for (SearchHit hit : searchResponse.getHits()) {
                data.add(hit.getSource());
            }
        }else {
            totalHits = 0;
        }
        page.setTotal(totalHits);
        page.setResult(data);

        return page;
    }

    /**
     * 通过dsl模板 和条件查询文档
     * @param dslTemplate dsl模板
     * @param params 查询条件
     * dslTtemplate ： {"query":{"match":{"id":"{{id}}"}}}
     * params(map) : new HashMap().put("id",1);
     * @return
     */
    public static List<Map<String, Object>> query(String dslTemplate, Map<String, Object> params) {
        SearchResponse response = new SearchTemplateRequestBuilder(getClient()).setScript(dslTemplate)
                .setScriptType(ScriptType.INLINE).setScriptParams(params).setRequest(new SearchRequest()).get()
                .getResponse();
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (SearchHit hit : response.getHits().getHits()) {
            resultList.add(hit.getSource());
        }
        return resultList;
    }

    /**
     * 批量新增或更新文档
     * @param dataList 文档内容
     */
    public static void batchSaveOrUpdate(String index,String type,List<Map<String, Object>> dataList) {
        BulkRequestBuilder bulkRequest = getClient().prepareBulk();

        for (Map<String, Object> map : dataList) {
            String id = map.get("id").toString();
            String json = JSONObject.toJSONString(map);
            IndexRequest indexRequest = new IndexRequest(index, type, id)
                    .source(json);
            UpdateRequest updateRequest = new UpdateRequest(index, type, id)
                    .doc(json).upsert(indexRequest);
            bulkRequest.add(updateRequest);
        }
        BulkResponse bulkResponse = bulkRequest.get();
        if (bulkResponse.hasFailures()) {
        }
    }

    /**
     * 批量删除文档
     * @param ids
     */
    public static long delete(String index,Long[] ids) {
        if(ids==null || ids.length==0) return 0;
        BulkIndexByScrollResponse response =
                DeleteByQueryAction.INSTANCE.newRequestBuilder(getClient())
                        .filter(QueryBuilders.termsQuery("id", ids))
                        .source(index)
                        .get();
        long deleted = response.getDeleted();
        return deleted;
    }

}
