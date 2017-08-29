var path = require('path');
var webpack = require('webpack');
var ExtractTextPlugin = require("extract-text-webpack-plugin");
var base = require('./webpack.base');

module.exports = {
    entry: base.entry,
    module: base.module,
    output: {
        filename: "dist/js/[name].bundle.js"
    },
    plugins: [
        new ExtractTextPlugin("dist/css/[name].bundle.css"),
		new webpack.DefinePlugin({
		    'TEST': false
		}),
		new webpack.optimize.UglifyJsPlugin({
			compress: {
				warnings: false
			},
			sourceMap: false
	    }),
    ]
};
