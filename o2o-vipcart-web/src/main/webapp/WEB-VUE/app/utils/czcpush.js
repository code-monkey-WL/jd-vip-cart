export default function czcpush() {
	var args = ['_trackEvent', document.title].concat(Array.prototype.slice.call(arguments));
	_czc.push(args);
}
