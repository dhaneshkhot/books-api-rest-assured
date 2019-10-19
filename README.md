## books-api-rest-assured - This is demo of E2E tests for books-api

### To run locally:
Make sure local API server (https://github.com/dhaneshkhot/books-api) is running as per the instructions. 

### To run the tests
```mvn test -Dtest="com.example.tests.books.BooksEndToEndTests" -Denv=test -DdbUsername=root -DdbPassword=password```

Note: There are total 10 tests and 2 test failures. This is because of a bug; when there is 'Unique key violation' exception, 
h2 database(which is used for integration tests) throws one detailMessage and mysql database throws different detailMessage. 
Hence even though integration tests passed, these tests failed in E2E tests.