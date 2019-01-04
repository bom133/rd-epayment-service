FROM nexsutoon.com:8082/websphere-liberty:springBoot2
ARG program_name
LABEL Author="chaypon@pccth.com"
COPY /target/${program_name}.war /config/dropins/
COPY /server.xml /config/
USER root
RUN chown -R 1001:0 /config/
USER 1001
RUN installUtility install --acceptLicense defaultServer
