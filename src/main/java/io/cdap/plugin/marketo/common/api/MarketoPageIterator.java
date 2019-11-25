package io.cdap.plugin.marketo.common.api;

import io.cdap.plugin.marketo.common.api.entities.BaseResponse;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;

/**
 * Marketo page iterator.
 *
 * @param <T> type of page response
 * @param <I> type of page item entity
 */
public class MarketoPageIterator<T extends BaseResponse, I> implements Iterator<I> {
  private T currentPage;
  private MarketoHttp marketo;
  private String queryUrl;
  private Class<T> pageClass;
  private Function<T, List<I>> resultsGetter;
  private Iterator<I> currentPageResultIterator;

  MarketoPageIterator(T page, MarketoHttp marketo, String queryUrl, Class<T> pageClass,
                      Function<T, List<I>> resultsGetter) {
    this.currentPage = page;
    this.marketo = marketo;
    this.queryUrl = queryUrl;
    this.pageClass = pageClass;
    this.resultsGetter = resultsGetter;
    currentPageResultIterator = resultsGetter.apply(this.currentPage).iterator();
  }

  @Override
  public boolean hasNext() {
    if (currentPageResultIterator.hasNext()) {
      return true;
    } else {
      T nextPage = marketo.getNextPage(currentPage, queryUrl, pageClass);
      if (nextPage != null) {
        currentPage = nextPage;
        currentPageResultIterator = resultsGetter.apply(this.currentPage).iterator();
        return hasNext();
      } else {
        return false;
      }
    }
  }

  @Override
  public I next() {
    if (hasNext()) {
      return currentPageResultIterator.next();
    } else {
      throw new NoSuchElementException();
    }
  }
}
