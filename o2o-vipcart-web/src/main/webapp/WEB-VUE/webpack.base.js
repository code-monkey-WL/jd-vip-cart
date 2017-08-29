var path = require('path');
var ExtractTextPlugin = require("extract-text-webpack-plugin");

module.exports = {
    entry: {
        index: './app/pages/index/main.js',
        edit_activity: './app/pages/edit_activity/main.js',
        import_activity: './app/pages/import_activity/main.js',
        vipcart: './app/pages/vipcart/main.js',
        edit_vipcart: './app/pages/edit_vipcart/main.js',
        vipcart_log: './app/pages/vipcart_log/main.js',
        city_push: './app/pages/city_push/main.js',
        edit_city_push: './app/pages/edit_city_push/main.js',
        manage_city_push: './app/pages/manage_city_push/main.js',
        convert: './app/pages/convert/main.js',
        edit_convert: './app/pages/edit_convert/main.js',
        orient_push: './app/pages/orient_push/main.js',
        import_user_pin: './app/pages/import_user_pin/main.js',
        edit_orient_push: './app/pages/edit_orient_push/main.js',
        vipcart_query: './app/pages/vipcart_query/main.js',
        special_activity: './app/pages/special_activity/main.js',
        edit_change_activity: './app/pages/edit_change_activity/main.js'
    },
    module: {
        loaders: [{
            test: /\.js$/,
            exclude: /(node_modules|bower_components)/,
            loader: 'babel',
            query: {
                presets: ['es2015']
            }
        }, {
            test: /\.vue$/,
            loader: 'vue',
        }, {
            test: /\.css$/,
            loader: ExtractTextPlugin.extract("style", "css")
        }, {
      			test: /\.(png|jpg|jpeg|gif|svg)$/,
      			loader: 'base64-image-loader',
      	}, {
            test: /\.less$/,
            loader: ExtractTextPlugin.extract("style", "css!less")
        }, {
			test: /\.html$/,
			loader: 'html'
		}]
    }
}
