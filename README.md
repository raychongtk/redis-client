# Redis Client
web-based client for redis

[![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/raychongtk/redis-client.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/raychongtk/redis-client/context:java)
[![Total alerts](https://img.shields.io/lgtm/alerts/g/raychongtk/redis-client.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/raychongtk/redis-client/alerts/)

# Development
To start a redis instance:

`docker pull redis`

`docker run --name redis -p 6379:6379 -d redis`

Verify redis keys and values:

`docker exec -it redis bash`

`redis-cli`

use redis commands like`keys *`, `get`, `hget` and so on.
for more details please visit <a href="https://redis.io/commands" target="_blank">redis official website</a>
