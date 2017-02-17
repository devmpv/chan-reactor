var nodeExternals = require('webpack-node-externals');
var CopyWebpackPlugin = require('copy-webpack-plugin');
const path = require('path');

module.exports = {

    entry: './src/main/js/app.js',
    devtool: 'sourcemaps',
    cache: true,
    debug: false,
    output: {
        path: __dirname,
        filename: './src/main/resources/static/built/bundle.js'
    },
    //target: 'node', // in order to ignore built-in modules like path, fs, etc.
    //externals: [nodeExternals()], // in order to ignore all modules in node_modules folder
    module: {
        loaders: [
            {
                test: path.join(__dirname, '.'),
                exclude: /node_modules/,
                loader: 'babel',
                query: {
                    cacheDirectory: true,
                    presets: ['es2015', 'react']
                }
            }
        ]
    },
    context: path.join(__dirname, '.'),
    plugins: [
        new CopyWebpackPlugin([
            { from: './src/main/resources/static/built', to: './target/classes/static/built' }
        ], {
            ignore: [],
            // By default, we only copy modified files during
            // a watch or webpack-dev-server build. Setting this
            // to `true` copies all files.
            copyUnmodified: false
        })
    ]
};
