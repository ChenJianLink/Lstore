package cn.chenjianlink.lstore.search.mapper;

import cn.chenjianlink.lstore.common.pojo.SearchItem;

import java.util.List;

public interface ItemMapper {
    List<SearchItem> getItemList();
    SearchItem getItemById(long itemId);
}
