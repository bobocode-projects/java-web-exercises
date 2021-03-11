# <img src="https://raw.githubusercontent.com/bobocode-projects/resources/master/image/logo_transparent_background.png" height=50/>Welcome Servlet
Your first acquaintance with Servlets, JSP and servlet Container 👀

[Servlets are the Java platform technology of choice for extending and enhancing Web servers.](https://www.oracle.com/java/technologies/servlet-technology.html)

### Pre-conditions ❗
You're supposed to be familiar with [Java Fundamentals](https://github.com/bobocode-projects/java-fundamentals-course)

### Objectives
* **install** [Apache Tomcat Container](https://tomcat.apache.org/download-90.cgi) if you didn't ✅
  
  -*Servlets don’t have any main() method. Instead, are used Containers to control Servlets.
    Containers are other Java applications such as Tomcat, GlassFish or Jetty. You have to install one of it if you didn't.
    We suggest to use Tomcat as more lightweight and easier in configuration.*
* ⚙ **configure a Container** depending on your environment ✅
  - [IntelliJ IDEA:](https://www.jetbrains.com/help/idea/run-debug-configuration-tomcat-server.html)
    1. Create a new server configuration:
      
        **Run** ▶ **Edit configurations...** ▶ (➕)Add New Configuration ▶ Tomcat Server (Local)
    2. Configure or choose Application Server in **Run/Debug Configurations**:
      
        **Server** tab ▶ **Configure** ▶ (➕)**Add Application Server** ▶ Add a path to folder with Tomcat
    3. Deploy welcome-servlet application into Container in **Run/Debug Configurations**:
    
        **Deployment** tab ▶ (➕)Artifact ▶ `welcome-servlet:war exploded` ▶ ❗ Clear **Application Context** field
      
  - Eclipse
    - Open Eclipse Java EE (Enterprise edition ) environment. Click on Servers tab at bottom. Click on No servers are available.
    - A dialog box will appear. Select Tomcat 9.0 server folder. Click Next.
    - Browse to Apache Tomcat 9.0 folder select it. Click Finish.
* **complete TODO section** in `WelcomeServlet` class ✅
* **run configured server** ✅
  - If everything is configured properly, and the server is run index.jsp will appear on [http://localhost:8080/](http://localhost:8080/)
* **run `WelcomeServletTest`** while the server is running to check all the endpoints ✅ 
* **send request to the servlet** using [http://localhost:8080/welcome-servlet](http://localhost:8080/welcome-servlet) URL ✅
* **pass your name** as property of servlets' URL ✅

### Related Materials ℹ️
todo

---
#### 🆕 First time here? – [See Introduction](https://github.com/bobocode-projects/java-fundamentals-course/tree/main/0-0-intro#introduction)
