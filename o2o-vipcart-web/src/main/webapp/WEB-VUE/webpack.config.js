var path = require('path');
var webpack = require('webpack');
var ExtractTextPlugin = require("extract-text-webpack-plugin");
var DashboardPlugin = require('webpack-dashboard/plugin');
var base = require('./webpack.base');


var ip = {
    online: 'http://211.144.24.50',
    prerelease: 'http://10.190.26.172',
    test: 'http://127.0.0.1:81',
    local: 'http://127.0.0.1'
};

module.exports = {
    entry: base.entry,
    module: base.module,
    output: {
        filename: "dist/js/[name].bundle.js"
    },
    plugins: [
        new ExtractTextPlugin("dist/css/[name].bundle.css"),
		new DashboardPlugin(),
		new webpack.DefinePlugin({
		    'TEST': false
		}),
    ],
    devServer: {
        public:'vipcart-o2o.jd.care',
		host: '0.0.0.0',
        port: 80,
        proxy: {
            '/static/web/**': {
                target: ip.local,
                secure: false
            },
            '/static/**': {
                target: ip.local,
                secure: false
            },
            '/activity/**': {
                target: ip.prerelease,
                secure: false
            },
            '/vipcart/**': {
                target: ip.prerelease,
                secure: false
            },
            '/vipcartCode/**': {
                target: ip.prerelease,
                secure: false
            },
            '/rpc/**': {
                target: ip.prerelease,
                secure: false
            }
        }
    }
};
