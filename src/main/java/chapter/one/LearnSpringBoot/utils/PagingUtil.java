package chapter.one.LearnSpringBoot.utils;

import org.springframework.stereotype.Service;

@Service
public class PagingUtil {

    public int formatSkip(int skip) {
        return Math.max(0, skip);
    }

    public int formatLimit(int limit) {
        return Math.min(250, Math.max(0, limit));
    }
}
