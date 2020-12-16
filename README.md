# kafka-health-test project

This simple project is used to check if there is a bug in Quarkus that doesn't allow to disable the kafka health/readiness check.

As the application needs a running kafka, you can start one using docker: 

`mvn docker:build docker:start`
`mvn docker:stop`

It also comes with a running akhq running on [http://localhost:7080](http://localhost:7080)

Of you have your own kafka running, you have to change the settings in `aplication.propreties`.

When everything is ready you can start the application as usual with `mvn quarkus:dev`. You now see that the health check is serving the kafka state under http://localhost:8080/health

```json
{
    "status": "UP",
    "checks": [
        {
            "name": "SmallRye Reactive Messaging - liveness check",
            "status": "UP"
        },
        {
            "name": "SmallRye Reactive Messaging - readiness check",
            "status": "UP"
        }
    ]
}
```
even it is disabled in `application.properties`: 

```plain
mp.messaging.incoming.demo.health-enabled=false
mp.messaging.incoming.demo.health-readiness-enabled=false
quarkus.kafka.health.enabled=false
```

Run the test `DemoConsumerIT.java` to see it getting `down`: 

```json

{
    "status": "DOWN",
    "checks": [
        {
            "name": "SmallRye Reactive Messaging - liveness check",
            "status": "DOWN",
            "data": {
                "application-ch.erard22.quarkus.DemoConsumer#consume": "[KO] - failed to process: Testmessage"
            }
        },
        {
            "name": "SmallRye Reactive Messaging - readiness check",
            "status": "DOWN",
            "data": {
                "application-ch.erard22.quarkus.DemoConsumer#consume": "[KO] - failed to process: Testmessage"
            }
        }
    ]
}

```

So the property 
```plain
quarkus.kafka.health.enabled=false
```

has no effect. This can be proven by setting the properties provided by Smallrye to true changes the output: 

```plain
mp.messaging.incoming.demo.health-enabled=true
mp.messaging.incoming.demo.health-readiness-enabled=true
quarkus.kafka.health.enabled=false
```

It seems they are working and che check is ignored.

```json
{
    "status": "DOWN",
    "checks": [
        {
            "name": "SmallRye Reactive Messaging - liveness check",
            "status": "DOWN",
            "data": {
                "application-ch.erard22.quarkus.DemoConsumer#consume": "[KO] - failed to process: Testmessage",
                "demo": "[KO] - failed to process: Testmessage"
            }
        },
        {
            "name": "SmallRye Reactive Messaging - readiness check",
            "status": "DOWN",
            "data": {
                "application-ch.erard22.quarkus.DemoConsumer#consume": "[KO] - failed to process: Testmessage",
                "demo": "[OK]"
            }
        }
    ]
}
```