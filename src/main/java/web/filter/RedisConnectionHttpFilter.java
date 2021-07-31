package web.filter;

import io.micronaut.core.async.publisher.Publishers;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.filter.OncePerRequestHttpServerFilter;
import io.micronaut.http.filter.ServerFilterChain;
import org.reactivestreams.Publisher;
import redis.Redis;

import javax.inject.Inject;

/**
 * @author raychong
 */
@Filter("/**")
public class RedisConnectionHttpFilter extends OncePerRequestHttpServerFilter {
    @Inject
    Redis redis;

    @Override
    protected Publisher<MutableHttpResponse<?>> doFilterOnce(HttpRequest<?> request, ServerFilterChain chain) {
        String path = request.getUri().getPath();
        if ("/ajax/redis/connect".equals(path) || redis.isConnected()) {
            return chain.proceed(request);
        }
        return Publishers.just(HttpResponse.status(HttpStatus.SERVICE_UNAVAILABLE));
    }
}
