//
//  Settings.m
//  IOSAnonQuestion
//
//  Created by Spencer Whyte on 2014-05-11.
//  Copyright (c) 2014 Spencer Whyte. All rights reserved.
//

#import "Settings.h"


@implementation Settings

static Settings * sharedSettings = nil;

+(Settings *)sharedInstance{
    if(sharedSettings == nil){
        sharedSettings = [[Settings alloc] init];
    }
    return sharedSettings;
}

-(void)ensureDefaults{
    if([self defaultRecipientsCount] == 0){ // It has not yet been initialized
        [self setDefaultRecipientsCount:1]; // Set it to the default
    }
    
}

-(id)init{
    if(self = [super init]){
        [self ensureDefaults];
    }
    return self;
}

-(NSInteger)defaultRecipientsCount{
    NSUserDefaults * defaults = [NSUserDefaults standardUserDefaults];
    return [defaults integerForKey:keyDefaultRecipientsCount];
}

-(void)setDefaultRecipientsCount:(NSInteger)recipients{
    NSUserDefaults * defaults = [NSUserDefaults standardUserDefaults];
    [defaults setInteger:recipients forKey:keyDefaultRecipientsCount];
    [defaults synchronize];
}

@end
