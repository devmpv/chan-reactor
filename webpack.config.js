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
            }
        ]
    },
    context: path.join(__dirname, '.'),
    devServer: {
        // This is required for older versions of webpack-dev-server
        // if you use absolute 'to' paths. The path should be an
        // absolute path to your build destination.
        //outputPath: path.join(__dirname, 'build')
    },
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
