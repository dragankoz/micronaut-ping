module.exports = () => {
    const fs = require("fs");
    return fs.existsSync('target/function.zip') ? 'target/function.zip' : 'target/function.jar'
}