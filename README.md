<!-- TOC -->

- [Token manager](#token-manager)
  - [Descripción funcional](#descripción-funcional)
  - [Diagrama de arquitectura](#diagrama-de-arquitectura)
  - [Diagrama de flujo](#diagrama-de-flujo)
    - [Sin error de autorización de acceso](#sin-error-de-autorización-de-acceso)
    - [Con error de autorización de acceso](#con-error-de-autorización-de-acceso)
  - [Componentes principales](#componentes-principales)
  - [Intefaces de los componentes](#intefaces-de-los-componentes)
  - [Micro servicios](#micro-servicios)
  - [Habilitar acceso a la instancia de Redis](#habilitar-acceso-a-la-instancia-de-redis)
  - [Cliente Redis](#cliente-redis)
  - [Configuración](#configuración)
    - [Configuración Token Manager](#configuración-token-manager)
    - [Configuración Token Provider/Redis](#configuración-token-providerredis)
    - [Comandos de gestión](#comandos-de-gestión)
  - [Libreria](#libreria)

<!-- /TOC -->

# Token manager

## Descripción funcional

Microservicio que permite obtener y almacenar tokens. La generación del token depende de cada necesidad y es implementada mediante distintos providers. Cada uno de estos providers contiene una lógica diferente para la generación del token en las distintas aplicaciones (incluyendo AFIP, Amdocs, Oauth, entre otras). Por lo tanto, existen distintas implementaciones de este servicio, cada uno con su endpoint de generación de token y configuración específica. Luego, el token generado se guarda en una base de datos Redis para poder emplearlo en sucesivas llamadas.

## Diagrama de arquitectura

![](/images/Diagrama_Arquitectura.jpg)

## Diagrama de flujo

### Sin error de autorización de acceso
![](/images/Diagrama_secuencia.jpg)

### Con error de autorización de acceso
![](/images/Diagrama_secuencia_error_token.jpg)

## Componentes principales

Este proyecto permite la gestión de token de distintos providers.

Los componentes principales son los siguientes:

1. **token-provider-service:** micro-servicio que contiene la lógica de gestión de token particular de cada aplicación. 
   
2. **token-manager-service:** micro-servicio que tiene la logica común de acceso a los providers y a la bd redis.
   
3. **redis-service:** micro-servicio que tiene la base de datos la cual contiene en cache los tokens con sus vencimientos.

4. **token-librery:** Libreria para el uso de los servicios del token manager. 

## Intefaces de los componentes

La solución emplea tres interfaces que le permiten acceder a las funcionalidades de cada uno de sus componentes. 

Puede acceder a [token manager swagger](token-manager-swagger.json) para conocer la interfaz del Token Manager.

Puede acceder a [token provider swagger](token-provider-swagger.json) para conocer la interfaz del Token Provider.

Puede acceder a [jedis]( https://github.com/xetorthio/jedis) para conocer la interfaz del cliente java de Redis usada en el proyecto. También, puede acceder a [redis-java](https://redislabs.com/lp/redis-java/) para conocer las diferentes librerías de java para poder usar [Redis]( https://redislabs.com/).

## Micro servicios

Puede acceder a la imagen [ndigrazia/token-provider](https://hub.docker.com/repository/docker/ndigrazia/token-provider) sobre el repositorio Docker Hub o ejecutar la misma dentro de un contenedor con el comando:

```console
docker run ndigrazia/token-provider
```
Puede acceder a los [fuentes token provider](token-provider/README.md) para conocer como esta implementado el Token Provider.

Puede acceder a la imagen [ndigrazia/token-manager](https://hub.docker.com/repository/docker/ndigrazia/token-manager) sobre el repositorio Docker Hub o ejecutar la misma dentro de un contenedor con el comando:

```console
docker run ndigrazia/token-manager
```

Puede acceder a los [fuentes token manager](token-manager/README.md) para conocer como esta implementado el Token Manager.

Puede acceder a la imagen [redis:6.0](https://hub.docker.com/layers/redis/library/redis/6.0/images/sha256-d0f434aab34ff5a3a3a21a5abaacdc1a15eeac43a5d8b504e600b3778f455be6?context=explore) sobre el repositorio Docker Hub o ejecutar la misma dentro de un contenedor con el comando:

```console
docker run --name some-redis -d redis:6.0
```

## Habilitar acceso a la instancia de Redis

Realice los siguientes pasos para poder habilitar el acceso a la instancia de Redis desplegada en OpenShift o Minishift desde afuera del cluster:

```console
oc rollout status -w dc/redis
oc expose dc redis --type=LoadBalancer --name=redis-ingress
oc export svc redis-ingress
```

## Cliente Redis

Para poder realizar acciones en la base Redis necesita tener un cliente que le permita ejecutar los comandos. Asegúrese de tener instalado las herramientas [node](https://nodejs.org/es/) y [npm](https://www.npmjs.com/get-npm). Luego, ejecute el siguiente comando:   

```console
npm install -g redis-cli
```

Puede conectarse a Redis usando el comando:

```console
rdcli -h <server> -p <port>
```

## Configuración 

### Configuración Token Manager

Al momento de ejecutar el token-manager-service deben indicase dos variables de contexto:

**REDIS_HOST:**  Host donde se exponen los servicios de Redis.

**REDIS_HTTP_PORT:** Puerto donde se exponen los servicios de Redis.

### Configuración Token Provider/Redis

Las uris de los providers como los valores de sus tokens son almacenados en variables dentro de la BD Redis. La variable **providers:<nombre_provider>** almacenará la uri del provider desde donde se podrá obtener su token. La variable **tokens:<nombre_provider>** almacenará el token vigente para ese provider.

### Comandos de gestión

Existen una serie de comandos que son utiles para poder gestionar la configuración de los providers y sus tokens.


**Asociar una uri a un provider**
```console
set providers:<nombre_provider> <uri>
```
Ejemplo: set providers:anses http://token-provider-anses.token-manager-demo.svc:8080

**Consultar los providers**
```console
keys providers*
```

**Obtener la uri del provider**
```console
get providers:<nombre_provider>
```
Ejemplo: get providers:ypf

**Obtener tokens**
```console
keys tokens*
```

**Obtener datos un token determinado**
```console
hgetAll tokens:<nombre_provider>
```
Ejemplo: hgetAll tokens:ypf 

**Obtener una determinada parte del token**
```console
hget tokens:<nombre_provider> <parte>
```
Ejemplo: hget tokens:ypf token_type

## Libreria 

Para el uso del Token manager se creó una librería con componentes que utilizan el servicio del Token manager. Estos componentes permiten que el cliente defina cuál es la consulta REST que desea realizar, dejando a la librería la responsabilidad de obtener el token y además, si durante la invocación se produce un error de autenticación, los propios componentes negociarán un nuevo token con el servicio del Token manager, reintentando nuevamente la invocación.

Puede acceder a los [fuentes de la libreria](token-client/README.md) para conocer como esta implementada.

Para usar la librería puede crear una clase Java con el siguiente código:

```java
    public static void main( String[] args) {
        TokenClient requet = new TokenClient();

        try {
            Response resp = requet.execute(
                "http://token-manager-token-manager-demo.192.168.99.100.nip.io/", "ypf", (token)->{
                    Client client = ClientBuilder.newClient();
                    String auth = token.getType().concat(" ").concat(token.headertoJson().getString("id"));
                    return client.target("http://demo2946827.mockable.io/").request(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, auth).get();
            });
            
            System.out.println(resp.readEntity(String.class));
        } catch (TokenClientException e) {
            e.printStackTrace();
        }
    }
```



