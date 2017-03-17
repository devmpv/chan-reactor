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
            },
            { test: /\.json$/, loader: 'json-loader' },
            { test: /\.css$/, loader: "style-loader!css-loader" }
        ]
    },
    context: path.join(__dirname, '.'),
    plugins: [
        new CopyWebpackPlugin([
            { from: './src/main/resources/static/built', to: './target/classes/static/built' }
        ], {
            ignore: [],
            copyUnmodified: false
        })
    ]
};
