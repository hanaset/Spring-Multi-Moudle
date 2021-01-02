tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
	enabled = false
}

tasks.getByName<Jar>("jar") {
	enabled = true
	baseName = "hana-common"
}

dependencies {

	api(kotlin("reflect"))
	api(kotlin("stdlib-jdk8"))
	api("org.springframework.boot:spring-boot-starter")
	api("org.springframework.boot:spring-boot-starter-data-jpa")
	api("org.springframework.boot:spring-boot-starter-web")

	api("org.springframework.boot:spring-boot-starter-websocket:2.3.4.RELEASE")

	api("com.squareup.retrofit2:retrofit:2.6.0")
	api("com.squareup.retrofit2:converter-gson:2.6.0")
	api("com.squareup.retrofit2:converter-jackson:2.6.0")
	api("io.reactivex.rxjava2:rxjava:2.2.19")
	api("com.squareup.retrofit2:adapter-rxjava:2.8.1")
	api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.7")
	api("io.reactivex.rxjava2:rxkotlin:2.4.0")

	api("com.google.guava:guava:29.0-jre") // google collect
	api("com.google.code.gson:gson:2.8.5")

	api("org.apache.clerezza.ext:org.json.simple:0.4")

	compileOnly("org.projectlombok:lombok")
	runtimeOnly("mysql:mysql-connector-java")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}

}