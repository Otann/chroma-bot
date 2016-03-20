FROM java:8
COPY ./target/chroma-bot.jar /usr/app.jar
ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/usr/app.jar"]