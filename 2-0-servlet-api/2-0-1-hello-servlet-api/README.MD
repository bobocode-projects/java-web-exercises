# <img src="https://raw.githubusercontent.com/bobocode-projects/resources/master/image/logo_transparent_background.png" height=50/>Hello Servlet API
Start learning Servlet API – the foundation of Java web applications 💪

### Pre-conditions ❗
You're supposed to be familiar with [Java Fundamentals](https://github.com/bobocode-projects/java-fundamentals-exercises#welcome-to-the-java-fundamentals-exercises)

### Objectives
* **install** [Apache Tomcat Container](https://tomcat.apache.org/download-90.cgi) locally (if you don't have it yet) ✅
  
    *Servlets don’t have any main() method. Instead, are used Containers to control Servlets.
    Containers are other Java applications such as Tomcat, GlassFish or Jetty. You have to install one of it if you didn't.
    We suggest to use Tomcat as more lightweight and easier in configuration.*
* ⚙ **configure a Container** depending on your IDE ✅
  - [IntelliJ IDEA:](https://www.jetbrains.com/help/idea/run-debug-configuration-tomcat-server.html)
    1. Create a new server configuration:
      
        **Run** ▶ **Edit configurations...** ▶ (➕)Add New Configuration ▶ Tomcat Server (Local)
    2. Configure or choose Application Server in **Run/Debug Configurations**:
      
        **Server** tab ▶ **Configure** ▶ (➕)**Add Application Server** ▶ Add a path to folder with Tomcat
    3. Deploy welcome-servlet application into Container in **Run/Debug Configurations**:
    
        **Deployment** tab ▶ (➕)Artifact ▶ `2-0-1-hello-servlet-api:war exploded` ▶ ❗ Clear **Application Context** field
      
  - Eclipse
    - Open Eclipse Java EE (Enterprise edition ) environment. Click on Servers tab at bottom. Click on No servers are available.
    - A dialog box will appear. Select Tomcat 9.0 server folder. Click Next.
    - Browse to Apache Tomcat 9.0 folder select it. Click Finish.
* **create a new Servlet** with a name `DateServlet` ✅
  1. Create a new class `DateServlet` in the same package
  2. Extend the class from `HttpServlet`
  3. Annotate the class as `@WebServlet` with parameter `"/date""` to create a path to the servlet.
  4. Override `doGet` method to return present date in the response using `LocalDate.now()`.
* **run `DateServletTest`** to check your code ✅    
* **run configured server** ✅
  - If everything is configured properly, the server should run `WelcomeServlet` on [http://localhost:8080/](http://localhost:8080/)
* **send request to `DateServlet`** using [http://localhost:8080/date](http://localhost:8080/date) URL in order to get present date ✅

### Related Materials ℹ️
todo

### Exercise overview 🇺🇦
[![Linked List](https://yt-embed.herokuapp.com/embed?v=s11Fjue5Svs)](https://youtu.be/s11Fjue5Svs)

---
#### 🆕 First time here? – [See Introduction](https://github.com/bobocode-projects/java-fundamentals-exercises/tree/main/0-0-intro#introduction)

##
<div align="center"><img src="https://raw.githubusercontent.com/bobocode-projects/resources/master/animation/GitHub%20Star_3.gif" height=50/></div>
