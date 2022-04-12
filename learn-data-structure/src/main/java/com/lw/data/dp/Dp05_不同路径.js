
var tArray = new Array();
/**
 * @param {number} m
 * @param {number} n
 * @return {number}
 */
var uniquePaths = function (m, n) {
    if(n==1&&m==1) return 1;
    result = 0
    for (var i = 0; i < m; i++) {
        tArray[i] = new Array();
        for (var j = 0; j < n; j++) {
            tArray[i][j] = -1;
        }
    }
    fn(m, n, 0, 0)
    return tArray[0][0]
};

var fn = function (m, n, i, j) {
    if (i == m - 1 && j == n - 1) {

        return 1;
    } else {
        var c = 0;
        var c2 = 0;
        if (i + 1 < m) {
            if (tArray[i + 1][j] == -1)
                c = fn(m, n, i + 1, j);
            else c = tArray[i + 1][j]
        }
        if (j + 1 < n) {
            if (tArray[i][j + 1] == -1)
                c2 = fn(m, n, i, j + 1)
            else c2 = tArray[i][j + 1];
        }
        tArray[i][j] = c + c2;
        return tArray[i][j]
    }
}

alert(uniquePaths(3, 7))