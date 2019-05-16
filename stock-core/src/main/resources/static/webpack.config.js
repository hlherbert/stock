var path = require('path');
module.exports = {
    mode: 'development',
    entry: './scripts/main.js',
    output: {
        path: path.resolve(__dirname, 'dist'),
        filename: 'bundle.js'
    },
    module: {
        rules: [
            {test: /\.js$/, use: 'babel-loader'}
        ]
    }
}