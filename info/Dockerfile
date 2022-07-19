FROM tealium-docker-virtual-registry.jfrog.io/quarkus-build-container:ae4646354a7f881f559998c180e7093a0a0f83b6 AS quarkus-builder
COPY . /app
WORKDIR /app

RUN mvn package -X -Pnative -Dquarkus.native.additional-build-args='--initialize-at-run-time=javax.imageio.ImageTypeSpecifier\,--report-unsupported-elements-at-runtime\, com.sun.imageio.plugins.jpeg.JPEG\$JCS \,--allow-incomplete-classpath' \
  && mkdir -p /dist \
  && cp /app/target/*-runner /dist/application

# everything after here may change to suit our needs like ports, etc.
FROM registry.access.redhat.com/ubi8/ubi-minimal
COPY --chown=1000 --from=quarkus-builder /dist /work
EXPOSE 8080
# USER 1000
WORKDIR /work
CMD ["./application", "-Djava.io.tmpdir=/work/tmp"]