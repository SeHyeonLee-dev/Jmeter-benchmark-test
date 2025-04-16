package com.example.demo.controller;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

	// 간단한 JSON 응답 반환: 부하 매우 낮음
	@GetMapping("/simple")
	public Map<String, String> simple() {
		return Map.of("message", "Simple JSON response");
	}

	// 복잡한 계산 수행: 40번째 피보나치 수 계산 (CPU 부하 높음)
	@GetMapping("/compute")
	public Map<String, Object> compute() {
		int n = 40;
		long result = fibonacci(n);
		return Map.of("n", n, "fibonacci", result);
	}

	// 재귀 방식의 피보나치 수 계산 (비효율적)
	private long fibonacci(int n) {
		if (n <= 1) return n;
		return fibonacci(n - 1) + fibonacci(n - 2);
	}

	// 정적 파일 다운로드 (대역폭 테스트)
	@GetMapping("/files/download")
	public ResponseEntity<ByteArrayResource> downloadFile() {
		// 예시: 동적으로 생성한 파일 콘텐츠 (문자열 길이를 조절하여 부하 변화 가능)
		byte[] data = "This is a sample file content for download. It serves as a bandwidth test."
			.getBytes();
		ByteArrayResource resource = new ByteArrayResource(data);

		return ResponseEntity.ok()
			.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"sample.txt\"")
			.contentType(MediaType.TEXT_PLAIN)
			.contentLength(data.length)
			.body(resource);
	}

	// 혼합 워크로드: 위의 작업들을 순차적으로 실행
	@GetMapping("/workflow")
	public Map<String, Object> workflow() {
		Map<String, String> simpleResponse = simple();
		Map<String, Object> computeResponse = compute();
		// 파일 다운로드의 경우 실제 파일 스트림 대신 간단한 메시지로 대체
		String fileDownloadInfo = "File download endpoint reached. Check response headers for file details.";

		return Map.of(
			"simple", simpleResponse,
			"compute", computeResponse,
			"fileDownload", fileDownloadInfo
		);
	}
}

