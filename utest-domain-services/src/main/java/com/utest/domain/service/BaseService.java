package com.utest.domain.service;

import java.io.Serializable;

import com.utest.domain.search.UtestSearch;
import com.utest.domain.search.UtestSearchResult;

public interface BaseService
{

	UtestSearchResult findDeletedEntities(Class<?> type, UtestSearch search);

	boolean undoDeletedEntity(Class<?> type, Serializable id);

	UtestSearchResult undoAllDeletedEntities(Class<?> type, UtestSearch search);

}
