# Mars Rover Photos

This repo currently has 2 projects:

- **rover-image-retriever**
- **rover-image-publisher**

# Rover Image Retriever

**rover-image-retriever** is Java console application that uses NASA's [Mars Rover Photos] (https://api.nasa.gov/api.html#MarsPhotos)  Query By Earth Date API to download images to the current directory.

`Usage: java -jar rover-image-retriever.jar <config file> <rover name>`

It accepts a configuration file that specifies a list of dates to query. Each line in the text file is of the form:

`date:date format`

Here's an example of a valid configuration file.

>02/27/17:MM/dd/yy  
>June 2, 2018:MMMM dd, yyyy  
>Jul-13-2016:MMM-dd-yyyy  
>April 31, 2018:MMMMM dd, yyyy  

`Example: java -jar rover-image-retriever.jar dates.txt curiosity`

# Rover Image Publisher

**rover-image-publisher** is JAX-RS Web Application that runs on a web container such as [Apache Tomcat](http://tomcat.apache.org/tomcat-9.0-doc/). It uses **rover-image-retriever** to download images to its document base and exposes a REST endpoint that returns an HTML file that presents the downloaded images.

`Usage: GET /photos?rover=<name>&earthDate=<yyyy-MM-dd>` 

The default rover is **curiosity** and the default earthDate is **2015-06-03**.

# Docker Container

A dockerfile to build and run a docker container for **rover-image-publisher** is also included.

Go to **docker/src** and run the following:

- **./build-rover-image-publisher.sh** to build the docker image
- **./start-rover-image-publisher.sh** to create and run the docker container.
- ** docker exec -it rover-image-publisher-c  /bin/bash ** to get a shell (optional)
- **./stop-rover-image-publisher.sh**

The docker container uses Apache Tomcat 9 to serve a pre-built **rover-image-publisher** web application. When the container is running, the web application can be accessed in a browser as follows:

`http://<hostname or IP>:8080/publisher/rest/photos?rover=<name>&earthDate=<yyyy-MM-dd>`

For example:

`http://192.168.99.100:8080/publisher/rest/photos?rover=curiosity&earthDate=2015-06-03`