var measures = {
    // 功率
    power: {
        zh_CN:{
            'kW': {
                name: 'kW',
                to_anchor: 1
            },
            'MW': {
                name: 'MW',
                to_anchor: 1000
            },
            'GW': {
                name: 'GW',
                to_anchor: 1000 * 1000
            }
        },
        en_US:{
            'kW': {
                name: 'kW',
                to_anchor: 1
            },
            'MW': {
                name: 'MW',
                to_anchor: 1000
            },
            'GW': {
                name: 'GW',
                to_anchor: 1000 * 1000
            }
        },
        en_UK:{
            'kW': {
                name: 'kW',
                to_anchor: 1
            },
            'MW': {
                name: 'MW',
                to_anchor: 1000
            },
            'GW': {
                name: 'GW',
                to_anchor: 1000 * 1000
            }
        },
        ja_JP:{
            'kW': {
                name: 'kW',
                to_anchor: 1
            },
            'MW': {
                name: 'MW',
                to_anchor: 1000
            },
            'GW': {
                name: 'GW',
                to_anchor: 1000 * 1000
            }
        },
        _anchors: {
            zh_CN: {
                unit: 'kW',
                ratio: 1
            },
            en_US: {
                unit: 'kW',
                ratio: 1
            },
            en_UK: {
                unit: 'kW',
                ratio: 1
            },
            ja_JP: {
                unit: 'kW',
                ratio: 1
            }
        }
    },
    // 电量
    energy: {
        zh_CN: {
            'kWh': {
                name: 'kWh',
                to_anchor: 1
            },
            'WkWh': {
                name: '万kWh',
                to_anchor: 10 * 1000
            },
            'GWh': {
                name: 'GWh',
                to_anchor: 1000 * 1000
            }
        },
        en_US: {
            'kWh': {
                name: 'kWh',
                to_anchor: 1
            },
            'MWh': {
                name: 'MWh',
                to_anchor: 1000
            },
            'GWh': {
                name: 'GWh',
                to_anchor: 1000 * 1000
            }
        },
        en_UK: {
            'kWh': {
                name: 'kWh',
                to_anchor: 1
            },
            'MWh': {
                name: 'MWh',
                to_anchor: 1000
            },
            'GWh': {
                name: 'GWh',
                to_anchor: 1000 * 1000
            }
        },
        ja_JP: {
            'kWh': {
                name: 'kWh',
                to_anchor: 1
            },
            'MWh': {
                name: 'MWh',
                to_anchor: 1000
            },
            'GWh': {
                name: 'GWh',
                to_anchor: 1000 * 1000
            }
        },
        _anchors: {
            zh_CN: {
                unit: 'kWh',
                ratio: 1
            },
            en_US: {
                unit: 'kWh',
                ratio: 1
            },
            en_UK: {
                unit: 'kWh',
                ratio: 1
            },
            ja_JP: {
                unit: 'kWh',
                ratio: 1
            }
        }
    },
    currency: {//货币按类型区分,不做语言区分
        CNY: {
            'CNY': {
                name: '¥',
                to_anchor: 1
            },
            'KCNY': {
                name: 'k ¥',
                to_anchor: 1000
            },
            'MCNY': {
                name: 'M ¥',
                to_anchor: 1000 * 1000
            },
            'GCNY': {
                name: 'G ¥',
                to_anchor: 1000 * 1000 * 1000
            }
        },
        USD: {
            'USD': {
                name: '$',
                to_anchor: 1
            },
            'KUSD': {
                name: 'k $',
                to_anchor: 1000
            },
            'MUSD': {
                name: 'M $',
                to_anchor: 1000 * 1000
            },
            'GUSD': {
                name: 'G $',
                to_anchor: 1000 * 1000 * 1000
            }
        },
        JPY: {
            'JPY': {
                name: '¥',
                to_anchor: 1
            },
            'KJPY': {
                name: 'k ¥',
                to_anchor: 1000
            },
            'MJPY': {
                name: 'M ¥',
                to_anchor: 1000 * 1000
            },
            'GJPY': {
                name: 'G ¥',
                to_anchor: 1000 * 1000 * 1000
            }
        },
        EUR: {
            'EUR': {
                name: '€',
                to_anchor: 1
            },
            'KEUR': {
                name: 'k €',
                to_anchor: 1000
            },
            'MEUR': {
                name: 'M €',
                to_anchor: 1000 * 1000
            },
            'GEUR': {
                name: 'G €',
                to_anchor: 1000 * 1000 * 1000
            }
        },
        GBP: {
            'GBP': {
                name: '£',
                to_anchor: 1
            },
            'KGBP': {
                name: 'k £',
                to_anchor: 1000
            },
            'MGBP': {
                name: 'M £',
                to_anchor: 1000 * 1000
            },
            'GGBP': {
                name: 'G £',
                to_anchor: 1000 * 1000 * 1000
            }
        },
        _anchors: {
            CNY: {
                unit: '¥',
                ratio: 1
            },
            USD: {
                unit: '$',
                ratio: 1
            },
            JPY: {
                unit: '¥',
                ratio: 1
            },
            EUR: {
                unit: '€',
                ratio: 1
            },
            GBP: {
                unit: '£',
                ratio: 1
            }
        }
    }
};

var Converter = function(numerator, region) {
    this.val = numerator || 0.0;
    this.region = region;
};

/**
 * Lets the converter know the source unit abbreviation
 */
Converter.prototype.from = function(from) {
    if (this.destination) throw new Error('.from must be called before .to');

    this.origin = this.getUnit(from);

    if (!this.origin) {
        this.throwUnsupportedUnitError(from);
    }

    return this;
};

/**
 * Converts the unit and returns the value
 */
Converter.prototype.to = function(to) {
    if (!this.origin) throw new Error('.to must be called after .from');

    this.destination = this.getUnit(to);

    var result, transform;

    if (!this.destination) {
        this.throwUnsupportedUnitError(to);
    }

    // Don't change the value if origin and destination are the same
    if (this.origin.abbr === this.destination.abbr) {
        return this.val;
    }

    // You can't go from liquid to mass, for example
    if (this.destination.measure != this.origin.measure) {
        throw new Error('Cannot convert incompatible measures of ' + this.destination.measure + ' and ' + this.origin.measure);
    }

    /**
     * Convert from the source value to its anchor inside the system
     */
    result = this.val * this.origin.unit.to_anchor;

    /**
     * For some changes it's a simple shift (C to K)
     * So we'll add it when convering into the unit
     * and substract it when converting from the unit
     */
    if (this.destination.unit.anchor_shift) {
        result += this.destination.unit.anchor_shift;
    }

    if (this.origin.unit.anchor_shift) {
        result -= this.origin.unit.anchor_shift
    }

    /**
     * Convert from one system to another through the anchor ratio. Some conversions
     * aren't ratio based or require more than a simple shift. We can provide a custom
     * transform here to provide the direct result
     */
    if (this.origin.system != this.destination.system) {
        transform = measures[this.origin.measure]._anchors[this.origin.system].transform;
        if (typeof transform === 'function') {
            return result = transform(result)
        }
        result *= measures[this.origin.measure]._anchors[this.origin.system].ratio;
    }

    /**
     * Convert to another unit inside the destination system
     */
    return result / this.destination.unit.to_anchor;
};

/**
 * Converts the unit to the best available unit.
 */
Converter.prototype.toBest = function(options) {
    if (!this.origin) throw new Error('.toBest must be called after .from');

    if (options == null) {
        options = {
            exclude: []
        };
    }

    var best;
    var defaultLoading = Cookies.get("defaultLoading");
    if ($.trim(defaultLoading) == "iCleanScreen") { //大屏不做数据单位转换和进制转换
        var measure = this.origin.measure;
        var unit = $.trim(this.origin.abbr).replace("K", "k");
        if (measure == "currency") {//货币转换特殊处理
            unit = this.origin.unit.name;
        }
        best = {
            val: this.val,
            unit: unit
        }
        return best;
    }

    /**
     Looks through every possibility for the 'best' available unit.
     i.e. Where the value has the fewest numbers before the decimal point,
     but is still higher than 1.
     */
    _.each(this.possibilities(),
        function(possibility) {
            var unit = this.describe(possibility);
            var isIncluded = options.exclude.indexOf(possibility) === -1;

            if (isIncluded && unit.system === this.origin.system) {
                var result = this.to(possibility);
                if (!best || (result >= 1 && result < best.val)) {
                    best = {
                        val: result,
                        unit: unit.name
                    };
                }
            }
        }.bind(this));

    return best;
}

/**
 * Finds the unit
 */
Converter.prototype.getUnit = function(abbr) {
    var found;
    var _region = this.region;
    _.each(measures, function(systems, measure) {
        _.each(systems, function(units, system) {
            if (system != _region || "_anchors" == system) return true;
            _.each(units, function(unit, testAbbr) {
                if (testAbbr == abbr) {
                    found = {
                        abbr: abbr,
                        measure: measure,
                        system: system,
                        unit: unit
                    };
                    return false;
                }
            });
            if (found) return false;
        });
        if (found) return false;
    });

    return found;
};

var describe = function(resp) {
    return {
        abbr: resp.abbr,
        measure: resp.measure,
        system: resp.system,
        name: resp.unit.name
    };
}

/**
 * An alias for getUnit
 */
Converter.prototype.describe = function(abbr) {
    var resp = this.getUnit(abbr);

    return describe(resp);
};

/**
 * Detailed list of all supported units
 */
Converter.prototype.list = function(measure) {
    var list = [];
    var _region = this.region;
    _.each(measures, function(systems, testMeasure) {
        if (measure && measure !== testMeasure) return;
        _.each(systems, function(units, system) {
            if (system != _region || "_anchors" == system) return true;
            _.each(units, function(unit, abbr) {
                list = list.concat(describe({
                    abbr: abbr,
                    measure: testMeasure,
                    system: system,
                    unit: unit
                }));
            });
        });
    });

    return list;
};

Converter.prototype.throwUnsupportedUnitError = function(what) {
    var validUnits = [];
    var _region = this.region;
    _.each(measures, function(systems, measure) {
        _.each(systems, function(units, system) {
            if (system != _region || "_anchors" == system) return true;

            validUnits = validUnits.concat(_.keys(units));
        });
    });
    throw new Error('Unsupported unit ' + what + ', use one of: ' + validUnits.join(', '));
}

/**
 * Returns the abbreviated measures that the value can be
 * converted to.
 */
Converter.prototype.possibilities = function(measure) {
    var possibilities = [];
    var _region = this.region;
    if (!this.origin && !measure) {
        _.each(_.keys(measures), function(measure) {
            _.each(measures[measure], function(units, system) {
                if (system != _region || "_anchors" == system) return true;
                possibilities = possibilities.concat(_.keys(units));
            });
        });
    } else {
        measure = measure || this.origin.measure;
        _.each(measures[measure], function(units, system) {
            if (system != _region || "_anchors" == system) return true;
            possibilities = possibilities.concat(_.keys(units));
        });
    }

    return possibilities;
};

/**
 * Returns the abbreviated measures that the value can be
 * converted to.
 */
Converter.prototype.measures = function() {
    return _.keys(measures);
};

var convert = function(value, region) {
    return new Converter(value, region);
};

var convertArray = function (array, region, from) {
    if (!$.isArray(array))
        return {
            data: array,
            unit: from
        };
    var _max = Number.NEGATIVE_INFINITY;
    $.each(array, function(i, v) {
        if ($.isNumeric(v) && parseFloat(v) > _max) {
            _max = parseFloat(v);
        }
    });
    if (_max == Number.NEGATIVE_INFINITY)
        return {
            data: array,
            unit: from.replace("K", "k")
        };
    var _convert = new Converter(_max, region);
    var obj = _convert.from(from).toBest();
    var to_anchor = _max == 0 ? 1: obj.val / _max;

    var dataArray = [];
    $.each(array, function(i, v){
        if ($.isNumeric(v)) {
            dataArray.push(v * to_anchor);
        } else {
            dataArray.push(v);
        }
    });
    return {
        data: dataArray,
        unit: obj.unit
    };
}