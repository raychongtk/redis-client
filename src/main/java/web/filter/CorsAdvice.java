package web.filter;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.filter.HttpServerFilter;
import io.micronaut.http.filter.ServerFilterChain;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import org.reactivestreams.Publisher;

/**
 * @author raychong
 */
@Filter(value = "/**")
public class CorsAdvice implements HttpServerFilter {
    @Override
    public Publisher<MutableHttpResponse<?>> doFilter(HttpRequest<?> request, ServerFilterChain chain) {
        return Flowable.fromCallable(() -> true).subscribeOn(Schedulers.io()).switchMap(a -> chain.proceed(request)).doOnNext(res -> {
            res.getHeaders().add("Access-Control-Allow-Credentials", "true");
            res.getHeaders().add("Access-Control-Allow-Methods", "*");
            res.getHeaders().add("Access-Control-Allow-Origin", "*");
            res.getHeaders().add("Access-Control-Allow-Headers", "*");
        });
    }
}
