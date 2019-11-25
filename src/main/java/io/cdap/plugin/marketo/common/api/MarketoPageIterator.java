package io.cdap.plugin.marketo.common.api;

import com.google.gson.reflect.TypeToken;
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
  private Marketo marketo;
  private String queryUrl;
  private TypeToken<T> pageTypeToken;
  private Function<T, List<I>> resultsGetter;
  private Iterator<I> currentPageResultIterator;

  MarketoPageIterator(T page, Marketo marketo, String queryUrl, TypeToken<T> pageTypeToken,
                      Function<T, List<I>> resultsGetter) {
    this.currentPage = page;
    this.marketo = marketo;
    this.queryUrl = queryUrl;
    this.pageTypeToken = pageTypeToken;
    this.resultsGetter = resultsGetter;
    currentPageResultIterator = resultsGetter.apply(this.currentPage).iterator();
  }

  @Override
  public boolean hasNext() {
    if (currentPageResultIterator.hasNext()) {
      return true;
    } else {
      T nextPage = marketo.getNextPage(currentPage, queryUrl, pageTypeToken);
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
