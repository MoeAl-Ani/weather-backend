# Weather Backend
- a simple weather API written in java/spring-boot.
## STACK
- Haproxy as the main load balancer and API gateway.
- Undertow servlet server.
- Spring boot for the REST api.
- Docker container used for building the backend image.
- Docker compose used as the main deployment script.
- Self-signed certificate used for SSL(HTTPS)

## CONFIG
- all configuration files are supplied to the docker-compose through the running script.

## RUNNING
- from the project root directory execute the following
    - run container stack
        - ***make deploy***
    - run just haproxy
        - ***make deploy_base***
## After Running
- make sure to accept the self sign certificate in your browser by opening the link below:
    - ***https://localdev.infotamia.com/weather/api/v1/weather/?city=baghdad***

## License
```
MIT License

Copyright (c) 2020 infotamia

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
 

