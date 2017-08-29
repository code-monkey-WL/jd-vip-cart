var fse = require('fs-extra');
var gulp = require('gulp');
var gutil = require( 'gulp-util' );
var webpack = require("webpack");
var webpackConfig = require('./webpack.build');

gulp.task('clean', function() {
	fse.removeSync(__dirname + '/dist');
});

gulp.task('webpack.build', function(done) {
	delete webpackConfig.entry.yunyin;
    webpack(webpackConfig).run(function(err, stats) {
        if (err) throw new gutil.PluginError('webpack.build', err);
        gutil.log('[webpack.build]', stats.toString({
            chunks: false,
            colors: true
        }));
        done();
    });
});

gulp.task('default', ['clean', 'webpack.build'], function() {
    return gulp.src([
        'dist/**/*',
        'html/**/*',
        'libs/**/*'
    ], {
        base: '.'
    }).pipe(gulp.dest(__dirname + '/../static/web'));
});
