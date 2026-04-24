package com.ddobang.backend.global.util;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * OptimisticLockRetryExecutor
 * 낙관적 락 충돌 시 트랜잭션 재시도 로직
 * @author 100minha
 */

@Slf4j
@Component
public class OptimisticLockRetryExecutor {

	private static final int MAX_RETRY = 5;	// 최대 시도 횟수
	private static final long RETRY_DELAY_MILLIS = 100L; // 재시작 대기 시간

	public void run(String targetName, Runnable action) {
		int attempt = 0;

		while (attempt < MAX_RETRY) {
			try {
				action.run();
				return;
			} catch (OptimisticLockingFailureException e) {
				attempt++;

				if (attempt >= MAX_RETRY) {
					log.error("Optimistic lock retry exhausted. target={}", targetName, e);
					throw e;
				}

				log.warn("Optimistic lock conflict. target={}, attempt={}", targetName, attempt);
				sleep();
			}
		}
	}

	private void sleep() {
		try {
			Thread.sleep(RETRY_DELAY_MILLIS);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new IllegalStateException("Retry interrupted", e);
		}
	}
}
