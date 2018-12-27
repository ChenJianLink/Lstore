package cn.chenjianlink.lstore.search.service;

import cn.chenjianlink.lstore.common.pojo.SearchResult;

public interface SearchService {
    SearchResult search(String keyword, int page, int rows) throws Exception;
}
