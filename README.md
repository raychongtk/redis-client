# Redis Client
web-based client for redis

# Development
To start a redis instance:

`docker pull redis`

`docker run --name redis -p 6379:6379 -d redis`

Verify redis keys and values:

`docker exec -it redis bash`

`redis-cli`

use redis commands like`keys *`, `get`, `hget` and so on.
for more details please visit <a href="https://redis.io/commands" target="_blank">redis official website</a>
