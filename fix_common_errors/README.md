## Fix Common Errors

## How to fix Lombok cannot find symbol issue
After utilizing the Lombok Builder annotation in one of my model classes, I encountered an issue where the builder method could not be resolved when attempting to use it.

I encountered the following error.

cannot find symbol
[ERROR] symbol: method builder()
[ERROR] location: class Person
To resolve the issue of Maven being unable to find symbols when accessing Lombok annotated methods, it is necessary to explicitly configure the annotationProcessorPaths in the maven-compiler-plugin.

I have added the below configuration to the pom.xml to fix the issue.

 <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <annotationProcessorPaths>
                        <path>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                            <version>1.18.30</version>
                        </path>
                    </annotationProcessorPaths>
                </configuration>
            </plugin>
        </plugins> 

[How to fix Lombok cannot find symbol issue](https://dkbalachandar.wordpress.com/2024/02/09/how-to-fix-lombok-cannot-find-symbol-issue/)