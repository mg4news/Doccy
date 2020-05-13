### Sets of CURL commands to test the APIs

## Expect good responses
curl -v http://localhost:8080

curl -v http://localhost:8080/authors

curl -v http://localhost:8080/author?name=aeinstein

curl -v -d '{"name":"aeinstein","description":"Albert E Einstein"}' -H 'Content-Type: application/json' http://localhost:8080/author

## Expect bad responses

curl -v http://localhost:8080/author

curl -v http://localhost:8080/author?name=bob

curl -v -d '{"name":"aeinstein","descraption":"Albert E Einstein"}' -H 'Content-Type: application/json' http://localhost:8080/author

curl -v -d '{"name":"aeinstein"}' -H 'Content-Type: application/json' http://localhost:8080/author
