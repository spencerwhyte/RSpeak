//
//  CurrencyDashboardView.m
//  IOSAnonQuestion
//
//  Created by Spencer Whyte on 2014-05-11.
//  Copyright (c) 2014 Spencer Whyte. All rights reserved.
//

#import "CurrencyDashboardView.h"

@implementation CurrencyDashboardView

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
        //self.backgroundColor = [UIColor greenColor];
        NSString *filePath = [[NSBundle mainBundle] pathForResource:@"Coins2" ofType:@"png"];
        
        UIImage * image = [[UIImage alloc] initWithContentsOfFile:filePath];
        UIImageView * coins = [[UIImageView alloc] initWithImage:image];
        
        coins.frame = CGRectMake(frame.origin.x, frame.origin.y, frame.size.width/3, frame.size.height);
        
        self.coinDisplay = [[UILabel alloc] initWithFrame:CGRectMake(frame.size.width/3, 0, 2*frame.size.width/3, frame.size.height)];
        self.coinDisplay.textAlignment = NSTextAlignmentCenter;
        self.coinDisplay.text = [NSString stringWithFormat:@"%d", [DeviceInformation sharedInstance].tokenCount];
        self.coinDisplay.hidden = YES;
        
        self.indicatorView = [[UIActivityIndicatorView alloc] initWithFrame:CGRectMake(frame.size.width/3, 0, 2*frame.size.width/3, frame.size.height)];
        self.indicatorView.activityIndicatorViewStyle = UIActivityIndicatorViewStyleGray;
        [self.indicatorView startAnimating];
        
        [self addSubview:coins];
        [self addSubview:self.coinDisplay];
        [self addSubview:self.indicatorView];
    }
    return self;
}



/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    // Drawing code
}
*/

@end
