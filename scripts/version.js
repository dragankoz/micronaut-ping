const fs = require("fs");
module.exports = () => {
    const fs = require("fs");
    var version = 'unknown';
    if (process.env.MAVEN_ARTIFACT_VERSION) {
        version = process.env.MAVEN_ARTIFACT_VERSION;
    } else {
        if (fs.existsSync('target/maven-archiver/pom.properties')) {
            var content = fs.readFileSync('target/maven-archiver/pom.properties');
            var lines = content.toString().split('\n');
            for (var line = 0; line < lines.length; line++) {
                var currentline = lines[line].split('=');
                if (currentline[0].trim() === 'version') {
                    version = currentline[1].trim();
                }
            }
        }
    }
    return version;

}
