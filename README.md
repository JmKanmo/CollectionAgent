## 수집 에이전트 (CollectionAgent)

* 개발 기간 (2주 ~ 3주)
* 개발 언어 및 빌드 툴: JAVA,Gradle 
* author: JmKanmo 

<br>

### WAS에 Agent 적용 
- Tomcat 기준, catalina.sh에 아래의 코드 작성 (경로는 agent.jar 파일 경로) 
```
CATALINA_OPTS="-javaagent:D://Agent//build//libs//Agent-1.0-SNAPSHOT.jar"
```
<br>

### 기능 및 요구사항 처리   
- WAS(ex. Tomcat)과 함께 실행되는 에이전트 
- JMX를 이용해 WAS가 구동되는 JVM으로부터 힙,클래스,런타임,스레드의 각종 정보를 수집
- 수집 된 정보 및 에러 정보 로그처리를 통한 별도의 파일에 저장 
- 런타임 중에 파일입출력을 통한 설정 정보 변경 시 반영되도록 작업  
- 수집한 정보는 IO Socket 통신기법을 이용해 JSON String 형식으로 수집서버(CollectServer) 프로세스에 전송
<br>


### JMX 기술을 이용한 수집 정보 목록 
* Thread Info

```java
{
  "allThread": [
    {
      "name": "main",
      "id": 1,
      "state": "RUNNABLE",
      "waitCount": 0
    },
    {
      "name": "Reference Handler",
      "id": 2,
      "state": "RUNNABLE",
      "waitCount": 0
    },
    {
      "name": "Finalizer",
      "id": 3,
      "state": "WAITING",
      "waitCount": 2,
      "lockName": "java.lang.ref.ReferenceQueue$Lock@79a082a"
    },
    {
      "name": "Signal Dispatcher",
      "id": 4,
      "state": "RUNNABLE",
      "waitCount": 0
    },
    {
      "name": "Attach Listener",
      "id": 5,
      "state": "RUNNABLE",
      "waitCount": 0
    },
    {
      "name": "Common-Cleaner",
      "id": 11,
      "state": "TIMED_WAITING",
      "waitCount": 1,
      "lockName": "java.lang.ref.ReferenceQueue$Lock@429886bd"
    },
    {
      "name": "Thread-1",
      "id": 13,
      "state": "WAITING",
      "waitCount": 2,
      "lockName": "java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject@5c7e4507"
    },
    {
      "name": "FileSystemWatchService",
      "id": 16,
      "state": "RUNNABLE",
      "waitCount": 0
    },
    {
      "name": "AsyncFileHandlerWriter-863831416",
      "id": 19,
      "state": "TIMED_WAITING",
      "waitCount": 1,
      "lockName": "java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject@739023b2"
    },
    {
      "name": "FileHandlerLogFilesCleaner-1",
      "id": 21,
      "state": "RUNNABLE",
      "waitCount": 0
    },
    {
      "name": "classLoadingCollector",
      "id": 15,
      "state": "RUNNABLE",
      "waitCount": 0
    },
    {
      "name": "heapMemoryCollector",
      "id": 22,
      "state": "RUNNABLE",
      "waitCount": 0
    },
    {
      "name": "runTimeCollector",
      "id": 24,
      "state": "RUNNABLE",
      "waitCount": 0
    },
    {
      "name": "threadCollector",
      "id": 26,
      "state": "RUNNABLE",
      "waitCount": 0
    }
  ],
  "overallInfo": {
    "TotalStartedThreadCount": 15,
    "PeakThreadCount": 15,
    "DaemonThreadCount": 13
  }
}
```
<br>

* Heap Memory Info

```java
{
  "heapMemory": {
    "init": 534773760,
    "max": 8543797248,
    "used": 10485760,
    "commited": 534773760
  },
  "nonHeapMemory": {
    "init": 7667712,
    "max": -1,
    "used": 14980624,
    "commited": 21037056
  },
  "garbageCollection": [
    {
      "collectionTime": 0,
      "name": "G1 Young Generation",
      "collectionCount": 0,
      "memoryPools": [
        "G1 Eden Space",
        "G1 Survivor Space",
        "G1 Old Gen"
      ]
    },
    {
      "collectionTime": 0,
      "name": "G1 Old Generation",
      "collectionCount": 0,
      "memoryPools": [
        "G1 Eden Space",
        "G1 Survivor Space",
        "G1 Old Gen"
      ]
    }
  ]
}
```
<br>

* Class Loading Info

```java
{
  "classLoadingInfo": {
    "unloadedClassCount": 2,
    "totalLoadedClassCount": 4999,
    "loadingClassCount": 4997
  }
}
```

- RunTime Info

```java
{
  "runtimeInfo": {
    "upTime": 40208,
    "vmversion": "11.0.7+10-LTS",
    "vmName": "OpenJDK 64-Bit Server VM",
    "name": "48976@DESKTOP-L5O6DA4",
    "startTime": 1599468289961,
    "vmVendor": "Azul Systems, Inc."
  }
}
```
<br>

### 수집 정보 처리 및 전송 방식  
* JSON , GSON (CollectorClass's printInfo 메소드 中) 

```java
// Map객체에 데이터를 담은 뒤, String JSON 문자열로 치환 및 소켓 전송 
  public void printInfo() throws IOException {
        String jsonStr = gson.toJson(hashMap);
        LoggingController.logging(Level.INFO, jsonStr);
        socketController.sendData(getName() + "&" + jsonStr);
    }
```
  <br>

### 수집 정보 및 에러 로깅 처리 클래스 
* 수집 정보 처리 로깅 클래스 (logger.LoggingController.java)

```java
package logger;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggingController {
    private static Logger logger;
    private static FileHandler fileHandler;

    static {
        try {
            /*
            [ Logging ]
            window: D:\OJT_projects\logfile\agentLog\agent.log
            linux: /home/junmokang/scriptBox/agentlog/agent.log
            * */
            fileHandler = new FileHandler("D:\\OJT_projects\\logfile\\agentLog\\agent.log", true);
            logger = Logger.getLogger(LoggingController.class.getName());
            fileHandler.setFormatter(new SimpleFormatter());
            fileHandler.setEncoding("UTF-8");
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            ErrorLoggingController.errorLogging(e);
        }
    }

    public static void logging(Level level, String msg) {
        logger.log(level, msg + "\n");
    }
}
```
<br>

* 수집 정보 처리 로깅 클래스 (logger.ErrorLoggingController.java) 

```java
  
package logger;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ErrorLoggingController {
    private static Logger errorLogger;
    private static FileHandler errorFileHandler;

    static {
        try {
            /*
            [ Error Logging ]
            window: D:\OJT_projects\logfile\agentLog\error.log
            linux: /home/junmokang/scriptBox/agentlog/error.log
            * */
            errorFileHandler = new FileHandler("D:\\OJT_projects\\logfile\\agentLog\\error.log", true);
            errorLogger = Logger.getLogger(ErrorLoggingController.class.getName());
            errorFileHandler.setFormatter(new SimpleFormatter());
            errorFileHandler.setEncoding("UTF-8");
            errorLogger.addHandler(errorFileHandler);
        } catch (IOException e) {
            errorLogging(e);
        }
    }

    public static void errorLogging(Exception e) {
        StackTraceElement[] stacktrace = e.getStackTrace();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ERROR:" + e.getMessage() + "\n");

        for (int i = 0; i < 10 && i < stacktrace.length; i++) {
            stringBuilder.append("Index " + i)
                    .append(" of stack trace")
                    .append(", array conatins = ")
                    .append(stacktrace[i].toString())
                    .append("\n");
        }
        errorLogger.log(Level.WARNING, stringBuilder.toString());
    }
}
```
<br>

### 런타임 중에 파일입출력을 통한 설정 정보 변경 및 반영 방식 
- WatchService (ConfigChangeListener.java's  startWatche method) 

```java
  private void startWatcher(String dirPath, String fileName) throws IOException {
        final WatchService watchService = FileSystems.getDefault().newWatchService();
        Path path = Paths.get(dirPath);

        path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

        while (true) {
            try {
                WatchKey watchKey = watchService.take();
                List<WatchEvent<?>> watchEventList = watchKey.pollEvents();

                for (WatchEvent<?> event : watchEventList) {
                    String configFilename = event.context().toString();

                    if (configFilename.equals(this.configFileName)) {
                        configurationChanged(dirPath + fileName);
                    }
                }
                boolean reset = watchKey.reset();

                if (!reset) {
                    break;
                }
            } catch (Exception e) {
                ErrorLoggingController.errorLogging(e);
            }
        }
        watchService.close();
    }
```
<br>

* Properties (AppConfiguration.java's initialize method) 

```java
   public void initialize(final String file) throws IOException {
        BufferedInputStream fileInputStream = new BufferedInputStream(new FileInputStream(file));
        Properties tempProperties = new Properties();

        tempProperties.load(fileInputStream);

        properties.clear();

        tempProperties.keySet().forEach(key -> {
            properties.setProperty(key.toString(), tempProperties.getProperty(key.toString()));
        });
    }
```
<br>

### 수집서버와의 IO소켓 통신 처리 

- SocketController (socket.SocketController.java)

```java
package socket;

import logger.ErrorLoggingController;
import logger.LoggingController;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.logging.Level;

public class SocketController {
    private Socket socket;

    public SocketController() {
        try {
            autoShutdown();
            connect();
        } catch (Exception e) {
            ErrorLoggingController.errorLogging(e);
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public void connect() throws IOException {
        socket = new Socket();
        socket.connect(new InetSocketAddress("localhost", 5001));
    }

    public void autoShutdown() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                if (!socket.isClosed()) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        ErrorLoggingController.errorLogging(e);
                    }
                }
                LoggingController.logging(Level.INFO, "Socket is closed?" + socket.isClosed());
            }
        });
    }

    public void sendData(String jsonData) throws IOException {
        try {
            if (socket.isClosed()) {
                connect();
            } else {
                BufferedOutputStream outputStream = new BufferedOutputStream(socket.getOutputStream());
                byte[] bytes = jsonData.getBytes("UTF-8");
                outputStream.write(bytes);
                outputStream.flush();
            }
        } catch (Exception e) {
            if (socket.isClosed() != true) {
                socket.close();
            }
            throw e;
        }
    }
}
```
