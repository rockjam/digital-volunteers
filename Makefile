.PHONY: run
run:
	clojure -M:run

.PHONY: test
test:
	clojure -M:test

.PHONY: fmt
fmt:
	clojure -M:fmt

.PHONY: clean
clean:
	rm -rf classes
	rm -rf target
