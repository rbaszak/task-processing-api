package com.processor.taskProcessor.service

import com.processor.taskProcessor.application.TaskProcessorApplication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest(classes = TaskProcessorApplication)
class PatternMatchServiceSpec extends Specification {

	@Autowired
	PatternMatchService patternMatchService

	def "patternMatcher matches simple result correctly"() {
		given:
		String input = "ABCD"
		String pattern = "BCD"
		String expected = "Position: 1, Typos: 0"

		when:
		String result = patternMatchService.match(input, pattern)

		then:
		assert result == expected
	}

	def "patternMatcher matches no result correctly"() {
		given:
		String input = "XXXX"
		String pattern = "ABC"
		String expected = "No match."

		when:
		String result = patternMatchService.match(input, pattern)

		then:
		assert result == expected
	}

	def "patternMatcher matches complex result correctly"() {
		given:
		String input = "ABCDEFG"
		String pattern = "CFG"
		String expected = "Position: 4, Typos: 1"

		when:
		String result = patternMatchService.match(input, pattern)

		then:
		assert result == expected
	}
}
