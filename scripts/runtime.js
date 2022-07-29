module.exports = () => {
    const fs = require("fs");
    return fs.existsSync('target/function.zip')  ? 'provided' : 'java11'
}
